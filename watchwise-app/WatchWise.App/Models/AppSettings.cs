using System;

namespace WatchWise.App.Models
{
    public class AppSettings
    {
        public string ApiBaseUrl { get; set; } = "http://localhost:8080";
        public int ApiTimeoutSeconds { get; set; } = 30;
        public bool IsFirstRun { get; set; } = true;
        public DateTime? LastSyncTime { get; set; }
    }

    public class TraktSettings
    {
        public string? ClientId { get; set; }
        public string? AccessToken { get; set; }
        public string? RefreshToken { get; set; }
        public DateTime? TokenExpiryTime { get; set; }
    }
}