# 🎵 Emo-Tunes

**Emotional feedback based Music recommendation**

A Spring Boot application that integrates with Spotify's Web API to provide music recommendations based on emotional feedback. The application handles Spotify OAuth authentication and provides song search functionality.

## 🚀 Features

- **Spotify OAuth Integration**: Secure authentication using Spotify's OAuth 2.0 flow
- **Token Management**: Automatic access token generation and refresh
- **Song Search**: Search for tracks using Spotify's Web API
- **Song Information Extraction**: Parse and display detailed song metadata
- **RESTful API**: Clean REST endpoints for easy integration

## 🛠️ Tech Stack

- **Java 21**
- **Spring Boot 3.3.2**
- **Maven** for dependency management
- **Spotify Web API** for music data

## 📋 Prerequisites

Before running this application, make sure you have:

- Java 21 or higher installed
- Maven 3.6+ installed
- A Spotify Developer account
- Spotify App credentials (Client ID, Client Secret)

## 🔧 Setup Instructions

### 1. Spotify App Configuration

1. Go to the [Spotify Developer Dashboard](https://developer.spotify.com/dashboard)
2. Create a new app or use an existing one
3. Note your `Client ID` and `Client Secret`
4. Add `http://localhost:8080/callback` to your app's Redirect URIs

### 2. Application Configuration

Create an `application.yml` file in `src/main/resources/` with your Spotify credentials:

```yaml
spotify:
  client-id: your_spotify_client_id
  client-secret: your_spotify_client_secret
  redirect: http://localhost:8080/callback
```

**⚠️ Security Note**: Never commit your actual credentials to version control. Consider using environment variables in production.

### 3. Running the Application

```bash
# Clone the repository
git clone https://github.com/yourusername/stellarcompiler-emo-tunes.git
cd stellarcompiler-emo-tunes

# Run the application
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## 🎯 API Endpoints

### Authentication Flow

1. **Initiate Spotify Login**
   ```
   GET /spotify/login
   ```
   Redirects to Spotify's authorization page

2. **OAuth Callback**
   ```
   GET /callback?code={authorization_code}&state={state}
   ```
   Handles the callback from Spotify and exchanges the authorization code for access tokens

## 🏗️ Project Structure

```
src/main/java/org/tunes/
├── SpringMain.java                    # Main application class
├── auth/                             # Authentication related classes
│   ├── SpotifyCred.java             # Credentials service
│   ├── SpotifyTokenService.java     # Token management service
│   └── TokenInterface.java          # Token service interface
├── config/                          # Configuration classes
│   └── Credentials.java             # Spotify credentials configuration
├── controllers/                     # REST controllers
│   ├── SpotifyCallbackController.java  # OAuth callback handler
│   └── SpotifyLoginController.java     # Login initiation
└── services/                        # Business logic services
    ├── SongHandle.java              # Song data processing
    └── SpotifySearch.java           # Spotify API search functionality
```

## 🔄 How It Works

1. **User Authentication**: Users visit `/spotify/login` to start the OAuth flow
2. **Authorization**: Application redirects to Spotify for user consent
3. **Token Exchange**: Spotify redirects back with an authorization code
4. **API Access**: Application exchanges the code for access and refresh tokens
5. **Music Search**: Use tokens to search for songs and extract metadata

## 🎵 Example Usage

After successful authentication, the callback endpoint demonstrates:

- Access token retrieval
- Refresh token functionality  
- Song search (searches for "Saiyaara" by default)
- Song metadata extraction (title, artist, release date)

## 🔒 Security Features

- **OAuth 2.0 Flow**: Secure authentication without storing user passwords
- **Token Refresh**: Automatic access token renewal using refresh tokens
- **Base64 Encoding**: Secure credential transmission
- **State Parameter**: CSRF protection in OAuth flow

## 🚧 Current Status

This is a foundational implementation that includes:
- ✅ Spotify OAuth authentication
- ✅ Token management (access + refresh)
- ✅ Basic song search functionality
- ✅ Song metadata extraction

## 🔮 Future Enhancements

- **Emotion Analysis**: Integrate emotion detection for personalized recommendations
- **Playlist Creation**: Create playlists based on emotional state
- **User Profiles**: Store user preferences and listening history
- **Advanced Recommendations**: Machine learning-based suggestion engine
- **Frontend Interface**: Web UI for better user experience

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request. For major changes, please open an issue first to discuss what you would like to change.

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👥 Team

**Team Whiplash** - Initial development

## 📞 Support

If you encounter any issues or have questions, please open an issue on GitHub.

---

⭐ **Star this repository if you find it helpful!**
