# Adaptive-API-Gateway

A high-throughput API gateway built with ASP.NET Core, designed to apply rate limiting, load control, and resilience patterns to maintain system stability under heavy traffic.

## Features

- **Controllers**: Example `TestController` for API endpoint demonstration
- **Custom Middleware**: `RequestLoggingMiddleware` for logging HTTP requests and response times
- **Rate Limiter Service (Stub)**: Interface and placeholder for future rate limiting logic
- **Swagger/OpenAPI**: Enabled for API documentation and testing in development
- **Health Check Endpoint**: `/health` returns a simple status

## Project Structure

- `Controllers/` – API controllers (e.g., `TestController`)
- `Middleware/` – Custom middleware (e.g., `RequestLoggingMiddleware`)
- `Services/` – Service layer, including rate limiter stub
- `appsettings.json` – Application configuration
- `Program.cs` – Application entry point and pipeline setup

## Getting Started

1. **Build and run the project** using .NET 10.0 or later:
	```sh
	dotnet run --project AdaptiveApiGateway/AdaptiveApiGateway.csproj
	```
2. **Access Swagger UI** at `https://localhost:<port>/swagger` (in development)
3. **Test endpoints** like `/api/test` and `/health`

## Next Steps

- Implement actual rate limiting logic in `RateLimiter`
- Add more controllers and middleware for advanced gateway features
