using Microsoft.Maui.Controls;
using WatchWise.App.Models;
using WatchWise.App.Services;
using WatchWise.App.ViewModels;
using System.Collections.ObjectModel;

namespace WatchWise.App;

public partial class MainPage : ContentPage
{
    private readonly SearchViewModel _searchViewModel;
    private readonly WatchlistViewModel _watchlistViewModel;
    private readonly IWatchlistService _watchlistService;

    public MainPage()
    {
        InitializeComponent();
        
        // Initialize services
        var apiClient = new ApiClient();
        _watchlistService = new WatchlistService();
        
        // Initialize ViewModels
        _searchViewModel = new SearchViewModel(apiClient, _watchlistService);
        _watchlistViewModel = new WatchlistViewModel(_watchlistService);
        
        // Set up bindings
        SetupBindings();
    }

    private void SetupBindings()
    {
        // Bind search section to SearchViewModel
        QueryEntry.SetBinding(Entry.TextProperty, new Binding(nameof(SearchViewModel.SearchQuery), source: _searchViewModel));
        ResultsCollection.SetBinding(CollectionView.ItemsSourceProperty, new Binding(nameof(SearchViewModel.SearchResults), source: _searchViewModel));
        
        // Add property for showing/hiding results
        _searchViewModel.PropertyChanged += (s, e) =>
        {
            if (e.PropertyName == nameof(SearchViewModel.SearchResults))
            {
                OnPropertyChanged(nameof(HasResults));
                OnPropertyChanged(nameof(ShowNoResults));
            }
        };
        
        // Bind watchlist section to WatchlistViewModel
        WatchlistCollection.SetBinding(CollectionView.ItemsSourceProperty, new Binding(nameof(WatchlistViewModel.WatchlistItems), source: _watchlistViewModel));
        
        // Set binding context
        BindingContext = _searchViewModel;
    }

    public bool HasResults => _searchViewModel.SearchResults.Any() && !_searchViewModel.IsLoading;
    public bool ShowNoResults => !_searchViewModel.SearchResults.Any() && !_searchViewModel.IsLoading && !string.IsNullOrWhiteSpace(_searchViewModel.SearchQuery);
    public ObservableCollection<TitleLite> WatchlistItems => _watchlistViewModel.WatchlistItems;

    private async void OnLoadWatchlistClicked(object? sender, EventArgs e)
    {
        await _watchlistViewModel.LoadWatchlistAsync();
        
        // Update bindings
        OnPropertyChanged(nameof(WatchlistItems));
        
        // Add a simple demo item if watchlist is empty
        if (!_watchlistViewModel.WatchlistItems.Any())
        {
            var demoTitle = new TitleLite
            {
                Id = "tt0133093",
                Type = TitleType.MOVIE,
                Name = "The Matrix",
                Year = 1999,
                PosterUrl = "https://example.com/matrix.jpg"
            };
            await _watchlistService.AddToWatchlistAsync(demoTitle);
            await _watchlistViewModel.LoadWatchlistAsync();
        }
    }
}
