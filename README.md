# Media Store

A self-hosted web application for downloading, organizing, and streaming videos from YouTube, Facebook, Instagram, and TikTok. Built with Spring Boot and powered by yt-dlp.

![Java](https://img.shields.io/badge/Java-8-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.5.10-brightgreen)
![License](https://img.shields.io/badge/license-MIT-blue)

## Features

- **Multi-platform video downloads** — YouTube, Facebook, Instagram, and TikTok supported via [yt-dlp](https://github.com/yt-dlp/yt-dlp)
- **Tag system** — Create custom tags per user and classify videos for easy filtering
- **Reels viewer** — TikTok-style vertical video player with swipe/keyboard navigation
- **Video streaming** — HTTP Range support for seek and resume playback
- **Bulk import** — Upload a WhatsApp chat `.txt` file and extract all video links automatically
- **Smart filters** — Filter by duration (short/long), tagged/untagged, or by specific tag
- **User accounts** — Registration, login, and per-user video libraries with Spring Security
- **Background processing** — Videos are queued and downloaded asynchronously every 10 seconds

https://github.com/user-attachments/assets/f9668c35-fe23-4b5f-9532-eb3eaa7e5a71

## Tech Stack

| Layer | Technology |
|-------|------------|
| Backend | Spring Boot 2.5.10, Spring Security, Spring Data JPA |
| Frontend | Thymeleaf, vanilla JavaScript (ES6 modules), CSS |
| Database | H2 (embedded, TCP mode) |
| Downloads | yt-dlp + ffmpeg |
| Build | Maven |

## Prerequisites

- **Java 8** or higher
- **Maven 3.6+**
- **yt-dlp** — place `yt-dlp.exe` (Windows) or `yt-dlp` (Linux/Mac) in the `downloaders/` directory
- **ffmpeg** — required for TikTok video re-encoding; place in `downloaders/ffmpeg/`

## Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/your-username/media-store-springboot-version.git
cd media-store-springboot-version
```

### 2. Configure the application

Edit `src/main/resources/application.properties` as needed:

```properties
# Database path
spring.datasource.url=jdbc:h2:tcp://localhost:9092/./db/memedb

# Download tool path
ytDl=downloaders/yt-dlp

# Where videos are temporarily stored during download
videoTemporalPath=tf

# Final video storage directory
videoOutputPath=downloads
```

### 3. Start the H2 database server

The application connects to H2 in TCP mode. Start the H2 server before running the app, or switch to embedded mode by changing the JDBC URL.

### 4. Run the application

```bash
mvn spring-boot:run
```

The app will be available at `http://localhost:8080`.

## Usage

### Downloading videos

1. **Sign up** for an account at the home page
2. **Paste a link** from YouTube, Facebook, Instagram, or TikTok in the URL input box
3. The video enters a **SUBMITTED** queue and is downloaded in the background
4. Once downloaded, it appears in your video library

### Bulk import from WhatsApp

1. Export a WhatsApp chat as a `.txt` file
2. Drag and drop the file into the upload area on the dashboard
3. All valid video links are extracted and queued for download

### Organizing with tags

- Create custom tags from the sidebar (e.g. "funny", "music", "tutorial")
- Assign one or more tags to any video
- Filter your library by tag, duration, or tagged/untagged status

### Reels viewer

- Browse your videos in a fullscreen vertical player
- Navigate with **arrow keys** or swipe on mobile
- Download, delete, or manage tags directly from the viewer

## Project Structure

```
src/main/java/com/jwillservices/mediastore/
├── controller/
│   ├── AuthController.java        # Login, signup, session management
│   ├── VideoController.java       # Video CRUD and tag assignment
│   ├── VideoActionsController.java # Streaming and file downloads
│   ├── TagController.java         # Tag CRUD
│   ├── ClientController.java      # User management
│   └── MvcController.java         # Page routing
├── service/
│   ├── ClientService.java         # User business logic
│   ├── VideoService.java          # Video + tag filtering logic
│   └── TagService.java            # Tag operations
├── entity/
│   ├── Client.java                # User account
│   ├── Video.java                 # Video metadata + state
│   └── Tag.java                   # Classification tag
├── repository/                    # Spring Data JPA repositories
├── dto/                           # Data transfer objects
├── downloader/
│   ├── DownloadVideo.java         # yt-dlp command execution
│   ├── VideoHelper.java           # Scheduled download processor
│   ├── FileParser.java            # URL extraction from text files
│   └── Platform.java              # Supported platform detection
└── MediaStore.java                # App entry point + security config

src/main/resources/
├── templates/                     # Thymeleaf HTML pages
├── static/css/                    # Stylesheets
├── static/js/                     # Frontend modules
└── application.properties
```

## API Endpoints

### Authentication
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/public/signup` | Create new account |
| POST | `/public/login` | Log in |
| POST | `/api/signout` | Log out |

### Videos
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/videos?link={url}` | Submit a video link for download |
| POST | `/api/videos/file` | Upload `.txt` file with multiple links |
| GET | `/api/videos` | Get last 10 downloaded videos |
| GET | `/api/videos/reel?tag={filter}` | Get videos by filter (all, lte60, bt60, with, without, or tag ID) |
| DELETE | `/api/videos/{id}` | Delete a video |
| PATCH | `/api/videos/add/tag/{tagId}/video/{videoId}` | Assign tag to video |
| PATCH | `/api/videos/delete/tag/{tagId}/video/{videoId}` | Remove tag from video |

### Tags
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/tags` | Create a new tag |
| GET | `/api/tags` | List all your tags |
| PATCH | `/api/tags/{id}?name={newName}` | Rename a tag |
| DELETE | `/api/tags/{id}` | Delete a tag |

### Streaming
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/videoactions/{id}/stream` | Stream video (supports Range headers) |
| GET | `/api/videoactions/{id}/download` | Download video file |

## Supported Platforms

| Platform | Domains |
|----------|---------|
| YouTube | `youtube.com`, `youtu.be` |
| Instagram | `instagram.com`, `instagr.am` |
| TikTok | `tiktok.com` |
| Facebook | `facebook.com`, `fb.watch` |
