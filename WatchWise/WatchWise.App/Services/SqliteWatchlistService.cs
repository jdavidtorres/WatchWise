using Microsoft.Data.Sqlite;
using WatchWise.App.Models;

namespace WatchWise.App.Services;

public class SqliteWatchlistService : IWatchlistService
{
    private readonly string _dbPath;

    public SqliteWatchlistService(string? dbPath = null)
    {
        if (dbPath != null)
        {
            _dbPath = dbPath;
        }
        else
        {
            var appDataPath = Environment.GetFolderPath(Environment.SpecialFolder.LocalApplicationData);
            _dbPath = Path.Combine(appDataPath, "watchwise.db");
        }
        InitializeAsync().GetAwaiter().GetResult();
    }

    private async Task InitializeAsync()
    {
        using var connection = new SqliteConnection($"Data Source={_dbPath}");
        await connection.OpenAsync();

        var command = connection.CreateCommand();
        command.CommandText = @"
            CREATE TABLE IF NOT EXISTS Watchlist (
                Id TEXT PRIMARY KEY,
                Type TEXT NOT NULL,
                Name TEXT NOT NULL,
                Year INTEGER,
                PosterUrl TEXT,
                CreatedAt TEXT NOT NULL DEFAULT (datetime('now'))
            );
        ";
        await command.ExecuteNonQueryAsync();
    }

    public async Task<bool> AddToWatchlistAsync(TitleLite title)
    {
        if (title == null || string.IsNullOrWhiteSpace(title.Id))
            return false;

        using var connection = new SqliteConnection($"Data Source={_dbPath}");
        await connection.OpenAsync();

        var command = connection.CreateCommand();
        command.CommandText = @"
            INSERT OR IGNORE INTO Watchlist (Id, Type, Name, Year, PosterUrl)
            VALUES (@id, @type, @name, @year, @posterUrl)
        ";
        command.Parameters.AddWithValue("@id", title.Id);
        command.Parameters.AddWithValue("@type", title.Type.ToString());
        command.Parameters.AddWithValue("@name", title.Name);
        command.Parameters.AddWithValue("@year", title.Year.HasValue ? (object)title.Year.Value : DBNull.Value);
        command.Parameters.AddWithValue("@posterUrl", (object?)title.PosterUrl ?? DBNull.Value);

        var rows = await command.ExecuteNonQueryAsync();
        return rows > 0;
    }

    public async Task<bool> RemoveFromWatchlistAsync(string titleId)
    {
        if (string.IsNullOrWhiteSpace(titleId))
            return false;

        using var connection = new SqliteConnection($"Data Source={_dbPath}");
        await connection.OpenAsync();

        var command = connection.CreateCommand();
        command.CommandText = "DELETE FROM Watchlist WHERE Id = @id";
        command.Parameters.AddWithValue("@id", titleId);

        var rows = await command.ExecuteNonQueryAsync();
        return rows > 0;
    }

    public async Task<bool> IsInWatchlistAsync(string titleId)
    {
        if (string.IsNullOrWhiteSpace(titleId))
            return false;

        using var connection = new SqliteConnection($"Data Source={_dbPath}");
        await connection.OpenAsync();

        var command = connection.CreateCommand();
        command.CommandText = "SELECT COUNT(1) FROM Watchlist WHERE Id = @id";
        command.Parameters.AddWithValue("@id", titleId);

        var count = await command.ExecuteScalarAsync();
        return Convert.ToInt64(count) > 0;
    }

    public async Task<List<TitleLite>> GetWatchlistAsync()
    {
        using var connection = new SqliteConnection($"Data Source={_dbPath}");
        await connection.OpenAsync();

        var command = connection.CreateCommand();
        command.CommandText = "SELECT Id, Type, Name, Year, PosterUrl FROM Watchlist ORDER BY CreatedAt DESC";

        var results = new List<TitleLite>();
        using var reader = await command.ExecuteReaderAsync();
        while (await reader.ReadAsync())
        {
            results.Add(new TitleLite
            {
                Id = reader.GetString(0),
                Type = Enum.TryParse<TitleType>(reader.GetString(1), ignoreCase: true, out var titleType)
                    ? titleType
                    : TitleType.MOVIE,
                Name = reader.GetString(2),
                Year = reader.IsDBNull(3) ? null : reader.GetInt32(3),
                PosterUrl = reader.IsDBNull(4) ? null : reader.GetString(4)
            });
        }
        return results;
    }
}
