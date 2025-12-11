using System.Collections.ObjectModel;
using System.Text.Json;
using System.Linq;
using Microsoft.Extensions.Logging;
using Microsoft.Maui.Storage;
using WatchWise.App.Models;

namespace WatchWise.App;

public partial class WatchListPage : ContentPage
{
    private const string WatchListKey = "watchlist";
    private readonly ObservableCollection<WatchItem> _items = new();
    private readonly ILogger<WatchListPage> _logger;
    private bool _isDirty;

    public WatchListPage(ILogger<WatchListPage> logger)
    {
        InitializeComponent();
        _logger = logger;
        WatchListView.ItemsSource = _items;
        // Note: Event handler doesn't need unsubscription because _items is owned by this page.
        // When the page is disposed, _items will also be eligible for garbage collection.
        _items.CollectionChanged += (s, e) => _isDirty = true;
    }

    protected override void OnAppearing()
    {
        base.OnAppearing();
        var json = Preferences.Get(WatchListKey, string.Empty);
        if (!string.IsNullOrEmpty(json))
        {
            try
            {
                var loaded = JsonSerializer.Deserialize<List<WatchItem>>(json);
                if (loaded != null)
                {
                    _items.Clear();
                    foreach (var item in loaded)
                        _items.Add(item);
                    _isDirty = false; // Reset dirty flag after loading
                }
            }
            catch (JsonException ex)
            {
                // Failed to deserialize saved watchlist data. This can occur if:
                // - The data format changed between app versions
                // - The stored data was corrupted
                // Starting with an empty watchlist instead of crashing
                _logger.LogWarning(ex, "Failed to deserialize watchlist data. Starting with empty list.");
            }
        }
    }

    protected override void OnDisappearing()
    {
        base.OnDisappearing();
        // Only save if items have changed to avoid redundant serialization
        if (_isDirty)
        {
            // ObservableCollection serializes to JSON array (same as List)
            var json = JsonSerializer.Serialize(_items);
            Preferences.Set(WatchListKey, json);
            _isDirty = false;
        }
    }

    private void OnAddItem(object sender, EventArgs e)
    {
        var title = NewItemEntry.Text?.Trim();
        if (!string.IsNullOrEmpty(title))
        {
            _items.Add(new WatchItem { Title = title });
            NewItemEntry.Text = string.Empty;
        }
    }

    private void OnRemoveItem(object sender, EventArgs e)
    {
        if ((sender as Button)?.CommandParameter is WatchItem item)
            _items.Remove(item);
    }
}
