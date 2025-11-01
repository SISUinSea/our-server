(function () {
  const listEl   = document.getElementById('list');
  const statusEl = document.getElementById('status');
  const dbgEl    = document.getElementById('debug');

  const log = (...a) => {
    console.log(...a);
    if (dbgEl) dbgEl.textContent += a.join(' ') + '\n';
  };

  const status = (m) => {
    if (!statusEl) return;
    statusEl.textContent = m || '';
    statusEl.hidden = !m;
  };

  // /Page/List.html 기준으로 ../Posted/ 로 이동
  const BASE  = new URL('../Posted/', location.href).pathname;
  const INDEX = BASE + '_files.txt';

  (async () => {
    // 1) 목록 파일(_files.txt) 읽기
    const r = await fetch(INDEX, { cache: 'no-store' });
    log('GET', INDEX, r.status);
    if (!r.ok) throw new Error('_files.txt 로드 실패: ' + r.status);

    let text = await r.text();
    text = text.replace(/^\uFEFF/, ''); // BOM 제거

    const names = text
      .split(/\r?\n/)
      .map(s => s.replace(/^\uFEFF/, '').trim())
      .filter(Boolean);

    if (!names.length) {
      status('등록된 글이 없습니다.');
      return;
    }

    // 2) 각 글 파일 읽어서 {id, title, date} 만들기
    const items = [];

    for (const raw of names) {
      const name = raw.replace(/^\uFEFF/, '');
      const url  = BASE + encodeURIComponent(name);

      let res;
      try {
        res = await fetch(url, { cache: 'no-store' });
      } catch (e) {
        log('fetch error', url, e.message);
        continue;
      }

      if (!res.ok) {
        log('open fail', url, res.status);
        continue;
      }

      let body = (await res.text()).replace(/^\uFEFF/, '');
      const lines    = body.split(/\r?\n/);
      const nonEmpty = lines.map(l => l.trim()).filter(l => l.length > 0);

      // 1) 제목: 첫 번째 비어있는 줄
      const title = nonEmpty[0] || name.replace(/\.txt$/i, '');

      // 2) 날짜 원문: 두 번째 비어있는 줄 있으면
      let createdAtRaw = '';
      if (nonEmpty[1]) {
        const line2 = nonEmpty[1];
        if (/^date:/i.test(line2)) {
          createdAtRaw = line2.replace(/^date:\s*/i, '').trim();
        } else {
          // 그냥 "2025-10-30" 또는 "2025-10-30 12:30" 식으로 썼을 때
          createdAtRaw = line2.trim();
        }
      }

      // 날짜만 뽑아서 보여주기 (시간 버림)
      const createdAt = formatDateOnly(createdAtRaw);

      items.push({
        id: name.replace(/\.txt$/i, ''),
        title,
        date: createdAt  // 화면에 보여줄 값
      });
    }

    // 3) 렌더
    render(items);
    status(items.length ? '' : '읽을 수 있는 글이 없습니다.');
  })().catch(e => {
    console.error(e);
    status('오류: ' + e.message);
  });

  function render(items) {
    const frag = document.createDocumentFragment();

    for (const it of items) {
      const li = document.createElement('li');
      li.className = 'item';

      // 날짜가 있으면 점과 날짜를 같이, 없으면 제목만
      li.innerHTML = `
        <div class="post-item">
          <a class="post-title" href="/Page/View.html?id=${encodeURIComponent(it.id)}">
            ${escapeHtml(it.title)}
          </a>
          ${it.date ? `
            <div class="meta">
              <span class="dot">·</span>
              <span class="date">${escapeHtml(it.date)}</span>
            </div>
          ` : ``}
        </div>
      `;
      frag.appendChild(li);
    }

    listEl.appendChild(frag);
  }

  // 문자열 안에 시간까지 있어도 날짜만 뽑아서 "YYYY-MM-DD"로 돌려주는 함수
  function formatDateOnly(str) {
    if (!str) return '';
    const d = parseDateLike(str);
    if (!d) {
      // 파싱 실패하면 원문이 날짜라면 그대로 보여줘도 됨
      return str;
    }
    const pad = n => (n < 10 ? '0' + n : '' + n);
    const y = d.getFullYear();
    const m = pad(d.getMonth() + 1);
    const day = pad(d.getDate());
    return `${y}-${m}-${day}`;
  }

  // 여러 타입의 문자열 날짜를 Date로 바꿔보는 함수
  function parseDateLike(str) {
    // 1) ISO 8601 (2025-10-30T17:45:00+09:00)
    const iso = new Date(str);
    if (!isNaN(iso.getTime())) return iso;

    // 2) "YYYY-MM-DD HH:mm" / "YYYY-MM-DD HH:mm:ss"
    const m = str.match(/^(\d{4}-\d{2}-\d{2})[ T](\d{2}:\d{2})(?::(\d{2}))?$/);
    if (m) {
      const iso2 = `${m[1]}T${m[2]}:${m[3] || '00'}`;
      const d2 = new Date(iso2);
      if (!isNaN(d2.getTime())) return d2;
    }

    // 3) "YYYY-MM-DD"
    const m2 = str.match(/^(\d{4}-\d{2}-\d{2})$/);
    if (m2) {
      const d3 = new Date(`${m2[1]}T00:00:00`);
      if (!isNaN(d3.getTime())) return d3;
    }

    return null;
  }

  function escapeHtml(s) {
    return (s ?? '').replace(/[&<>"']/g, c => ({
      '&':'&amp;',
      '<':'&lt;',
      '>':'&gt;',
      '"':'&quot;',
      "'":'&#39;'
    }[c]));
  }

})();
