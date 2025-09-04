using System.Collections.ObjectModel;
using System.Windows.Input;
using WatchWise.App.Models;
using WatchWise.App.Services;

namespace WatchWise.App.ViewModels;

public class SearchViewModel : BaseViewModel
{
    private readonly IApiClient _apiClient;
    private readonly IWatchlistService _watchlistService;
    private string _searchQuery = string.Empty;
    private bool _isLoading = false;
    private TitleType? _selectedType = null;
    private CancellationTokenSource? _cancellationTokenSource;

    public SearchViewModel(IApiClient apiClient, IWatchlistService watchlistService)
    {
        _apiClient = apiClient;
        _watchlistService = watchlistService;
        SearchResults = new ObservableCollection<TitleLite>();
        SearchCommand = new Command(async () => await SearchAsync());
    }

    public string SearchQuery
    {
        get => _searchQuery;
        set => SetProperty(ref _searchQuery, value);
    }

    public bool IsLoading
    {
        get => _isLoading;
        set => SetProperty(ref _isLoading, value);
    }

    public TitleType? SelectedType
    {
        get => _selectedType;
        set => SetProperty(ref _selectedType, value);
    }

    public ObservableCollection<TitleLite> SearchResults { get; }

    public ICommand SearchCommand { get; }

    private async Task SearchAsync()
    {
        if (string.IsNullOrWhiteSpace(SearchQuery) || SearchQuery.Length < 2)
        {
            SearchResults.Clear();
            return;
        }

        _cancellationTokenSource?.Cancel();
        _cancellationTokenSource = new CancellationTokenSource();

        IsLoading = true;
        try
        {
            var results = await _apiClient.SearchTitlesAsync(SearchQuery, SelectedType, 0, _cancellationTokenSource.Token);
            
            SearchResults.Clear();
            foreach (var result in results)
            {
                SearchResults.Add(result);
            }
        }
        catch (OperationCanceledException)
        {
            // Search was cancelled, ignore
        }
        catch (Exception ex)
        {
            System.Diagnostics.Debug.WriteLine($"Search error: {ex.Message}");
        }
        finally
        {
            IsLoading = false;
        }
    }
}