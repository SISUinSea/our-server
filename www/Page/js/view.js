(function () {  // 바로 실행해서 전역오염 막기

  // 1) View.html 안에 있어야 할 요소들을 찾아온다
  const titleEl    = document.getElementById('title');     // 제목
  const dateEl     = document.getElementById('date');      // ← 날짜/시간 표시할 곳 (없으면 그냥 안 씀)
  const bodyEl     = document.getElementById('body');      // 본문
  const commentsEl = document.getElementById('comments');  // 댓글
  const statusEl   = document.getElementById('status');    // 상태/에러
  const dbgEl      = document.getElementById('debug');     // 디버그

  const log = (...a) => {
    console.log(...a);
    if (dbgEl) dbgEl.textContent += a.join(' ') + '\n';
  };

  const status = (m) => {
    if (!statusEl) return;
    statusEl.textContent = m || '';
    statusEl.hidden = !m;
  };

  // 2) URL에서 id 읽기
  const params = new URLSearchParams(location.search);
  const id = params.get('id');

  if (!id) {
    status('글 id가 없습니다. (예: /Page/View.html?id=1)');
    return;
  }

  const commentForm = document.getElementById('comment-form');
  if (commentForm) {
    // /Posted?id=글번호  형식으로 설정
    commentForm.action = `/Posted?id=${encodeURIComponent(id)}`;
  }
  
  // 3) 실제 글 파일 경로 만들기
  const FILE_URL = new URL(`../Posted/${encodeURIComponent(id)}.txt`, location.href).pathname;

  // 4) 글 가져오기
  (async () => {
    log('GET', FILE_URL);
    const res = await fetch(FILE_URL, { cache: 'no-store' });
    if (!res.ok) {
      status('글을 불러오지 못했습니다. (HTTP ' + res.status + ')');
      return;
    }

    // 5) 텍스트로 + BOM 제거
    let text = await res.text();
    text = text.replace(/^\uFEFF/, '');

    // 줄 단위
    const lines = text.split(/\r?\n/);

    // 비어있지 않은 줄만 모아서 제목/날짜 파싱에 쓴다
    const nonEmpty = lines.map(l => l.trim()).filter(l => l.length > 0);

    // ===== 제목 =====
    const title = nonEmpty[0] || id;

    // ===== 날짜/시간 (있을 수도, 없을 수도) =====
    let dateRaw = '';
    if (nonEmpty[1]) {
      const l2 = nonEmpty[1];
      if (/^date:/i.test(l2)) {
        dateRaw = l2.replace(/^date:\s*/i, '').trim();
      } else if (/^\d{4}-\d{2}-\d{2}/.test(l2)) {
        // 그냥 "2025-10-30 19:22" 같은 형식
        dateRaw = l2.trim();
      }
    }

    // 사람이 보기 좋게 변환 (YYYY-MM-DD HH:mm)
    const dateText = formatDateTime(dateRaw);

    // ===== 본문/댓글 파트 =====
    // 원래는 빈 줄 2개로 나눴는데, 이제 "위에서 제목+날짜를 이미 소비했는지"를 고려해야 한다.
    // 가장 간단한 방법: 전체 텍스트에서 "제목 줄"과 (있다면) "날짜 줄"을 빼고 나머지를 다시 섹션으로 쪼갠다.
    let leftover = text;

    // 제목 줄 지우기
    if (lines.length > 0) {
      leftover = leftover.replace(lines[0], '');
    }
    // 날짜 줄도 있었으면 지우기
    if (dateRaw && lines.length > 1) {
      // 두 번째 줄 원문이 뭐였는지 찾아서 빼기
      leftover = leftover.replace(lines[1], '');
    }

    // 나머지를 다시 빈 줄 기준으로 쪼갬
    const sections = leftover.split(/\r?\n\r?\n/).map(s => s.trim()).filter(Boolean);

    // 본문은 첫 번째 섹션으로 보고,
    // 댓글은 두 번째 섹션으로 본다.
    let body = '';
    let comments = [];

    if (sections.length >= 1) {
      body = sections[0];
    }
    if (sections.length >= 2) {
      comments = sections[1].split(/\r?\n/).map(s => s.trim()).filter(Boolean);
    }

    // ===== DOM에 반영 =====
    if (titleEl) titleEl.textContent = title;
    if (dateEl)  dateEl.textContent  = dateText;   // 날짜/시간 보여주기
    if (bodyEl)  bodyEl.textContent  = body;

    if (commentsEl) {
      commentsEl.innerHTML = '';
      if (!comments.length) {
        const p = document.createElement('p');
        p.textContent = '댓글이 없습니다.';
        commentsEl.appendChild(p);
      } else {
        const ul = document.createElement('ul');
        for (const c of comments) {
          const li = document.createElement('li');
          li.textContent = c;
          ul.appendChild(li);
        }
        commentsEl.appendChild(ul);
      }
    }

    status('');
  })().catch(err => {
    console.error(err);
    status('오류가 발생했습니다: ' + err.message);
  });

  // ===== 날짜 파싱/포맷 함수들 =====

  // 여러 문자열 형식을 Date로 바꿔보는 함수
  function parseDateLike(str) {
    if (!str) return null;

    // 1) ISO 형식: 2025-10-30T19:22:00+09:00
    const iso = new Date(str);
    if (!isNaN(iso.getTime())) return iso;

    // 2) "YYYY-MM-DD HH:mm" or "YYYY-MM-DD HH:mm:ss"
    const m = str.match(/^(\d{4}-\d{2}-\d{2})[ T](\d{2}:\d{2})(?::(\d{2}))?$/);
    if (m) {
      const iso2 = `${m[1]}T${m[2]}:${m[3] || '00'}`;
      const d2 = new Date(iso2);
      if (!isNaN(d2.getTime())) return d2;
    }

    // 3) "YYYY-MM-DD" 만 있는 경우
    const m2 = str.match(/^(\d{4}-\d{2}-\d{2})$/);
    if (m2) {
      const d3 = new Date(`${m2[1]}T00:00:00`);
      if (!isNaN(d3.getTime())) return d3;
    }

    return null;
  }

  // Date → "YYYY-MM-DD HH:mm" 으로
  function formatDateTime(str) {
    const d = parseDateLike(str);
    if (!d) return str || '';    // 파싱 실패하면 원문 그대로
    const pad = n => (n < 10 ? '0' + n : '' + n);
    const y  = d.getFullYear();
    const m  = pad(d.getMonth() + 1);
    const da = pad(d.getDate());
    const hh = pad(d.getHours());
    const mm = pad(d.getMinutes());
    return `${y}-${m}-${da} ${hh}:${mm}`;
  }

})();  // IIFE 끝
