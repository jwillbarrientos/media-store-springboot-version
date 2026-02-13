import { myFetch } from "./myfetch.js";

document.addEventListener('DOMContentLoaded', () => {
    const videosWithTagsBtn = document.getElementById('videosWithTagsBtn');
    if (!videosWithTagsBtn) return;

    videosWithTagsBtn.addEventListener('click', async() => {
        try {
            const response = await myFetch('/api/videos/reel?tag=with');

            if (!response.ok) {
                console.error(`Server returned ${response.status}`);
                return;
            }

            const videos = await response.json();
            console.log(`Fetched ${videos.length} videos (with)`);

            localStorage.setItem('selectedTag', 'with');
            localStorage.setItem('videoList', JSON.stringify(videos));

            window.location.href = '/reels.html';
        } catch (err) {
            console.error('Error fetching videos with tags: ', err)
        }
    });
});