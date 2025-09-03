using Microsoft.Data.Sqlite;
using System;
using System.IO;
using System.Threading.Tasks;

namespace WatchWise.App.Services
{
    public class SqliteService
    {
        private readonly string _dbPath;
        
        public SqliteService()
        {
            var appDataPath = Environment.GetFolderPath(Environment.SpecialFolder.LocalApplicationData);
            _dbPath = Path.Combine(appDataPath, "watchwise.db");
        }

        public async Task InitializeAsync()
        {
            using var connection = new SqliteConnection($"Data Source={_dbPath}");
            await connection.OpenAsync();

            var command = connection.CreateCommand();
            command.CommandText = @"
                CREATE TABLE IF NOT EXISTS AppMeta (
                    Key TEXT PRIMARY KEY,
                    Value TEXT,
                    LastUpdated DATETIME DEFAULT CURRENT_TIMESTAMP
                );

                CREATE TABLE IF NOT EXISTS UserPreferences (
                    Id INTEGER PRIMARY KEY AUTOINCREMENT,
                    Key TEXT UNIQUE,
                    Value TEXT,
                    CreatedAt DATETIME DEFAULT CURRENT_TIMESTAMP
                );
            ";
            
            await command.ExecuteNonQueryAsync();
        }

        public async Task<string?> GetAppMetaAsync(string key)
        {
            using var connection = new SqliteConnection($"Data Source={_dbPath}");
            await connection.OpenAsync();

            var command = connection.CreateCommand();
            command.CommandText = "SELECT Value FROM AppMeta WHERE Key = @key";
            command.Parameters.AddWithValue("@key", key);

            var result = await command.ExecuteScalarAsync();
            return result?.ToString();
        }

        public async Task SetAppMetaAsync(string key, string value)
        {
            using var connection = new SqliteConnection($"Data Source={_dbPath}");
            await connection.OpenAsync();

            var command = connection.CreateCommand();
            command.CommandText = @"
                INSERT OR REPLACE INTO AppMeta (Key, Value, LastUpdated) 
                VALUES (@key, @value, CURRENT_TIMESTAMP)
            ";
            command.Parameters.AddWithValue("@key", key);
            command.Parameters.AddWithValue("@value", value);

            await command.ExecuteNonQueryAsync();
        }
    }
}