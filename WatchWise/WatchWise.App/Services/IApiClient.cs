using WatchWise.App.Models;

namespace WatchWise.App.Services;

public interface IApiClient
{
    Task<List<TitleLite>> SearchTitlesAsync(string query, TitleType? type = null, int page = 0, CancellationToken cancellationToken = default);
    Task<TitleDetail?> GetTitleDetailsAsync(string id, CancellationToken cancellationToken = default);
}