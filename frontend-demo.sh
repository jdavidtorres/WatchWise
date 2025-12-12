#!/bin/bash

echo "=== WatchWise Frontend Integration Demo ==="
echo

echo "Backend API is running on: http://localhost:8080"
echo

echo "1. Testing title search for 'matrix':"
curl -s "http://localhost:8080/api/titles/search?q=matrix" | python3 -m json.tool
echo

echo "2. Testing title search with type filter (SHOW):"
curl -s "http://localhost:8080/api/titles/search?q=breaking&type=SHOW" | python3 -m json.tool
echo

echo "3. Testing title details (The Matrix):"
curl -s "http://localhost:8080/api/titles/tt0133093" | python3 -m json.tool
echo

echo "4. Testing 404 response for non-existent title:"
echo "HTTP Status Code: $(curl -s -w "%{http_code}" "http://localhost:8080/api/titles/nonexistent")"
echo

echo "=== Frontend Components Created ==="
echo "✅ Models: TitleType, TitleLite, TitleDetail"
echo "✅ Services: IApiClient, ApiClient, IWatchlistService, WatchlistService"  
echo "✅ ViewModels: BaseViewModel, SearchViewModel, TitleDetailViewModel, WatchlistViewModel"
echo "✅ Updated MainPage XAML with data binding and proper UI"
echo "✅ API integration with error handling and cancellation support"
echo

echo "=== Demo completed ==="
