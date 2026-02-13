import { myFetch } from "./myfetch.js";

document.querySelectorAll('.tag-name').forEach(span => {
    span.addEventListener('click', async () => {
        const tagId = span.dataset.tagId;
        try {
            const response = await myFetch(`/api/videos/reel?tag=${tagId}`);
            if (!response.ok) {
                console.error(`Server returned ${response.status}`);
                return;
            }

            const videos = await response.json();
            console.log(`Fetched ${videos.length} videos (${tagId})`);

            localStorage.setItem('selectedTag', tagId);
            localStorage.setItem('videoList', JSON.stringify(videos));

            window.location.href = '/reels.html';
        } catch (err) {
            console.error('Error fetching videos for tag: ', err);
        }
    })
})