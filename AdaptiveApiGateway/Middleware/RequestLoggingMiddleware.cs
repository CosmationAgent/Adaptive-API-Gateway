using System.Diagnostics;

namespace AdaptiveApiGateway.Middleware
{
    public class RequestLoggingMiddleware
    {
        private readonly RequestDelegate _next;
        private readonly ILogger<RequestLoggingMiddleware> _logger;

        public RequestLoggingMiddleware(RequestDelegate next, ILogger<RequestLoggingMiddleware> logger)
        {
            _next = next;
            _logger = logger;
        }

        public async Task InvokeAsync(HttpContext context)
        {
            var stopwatch = Stopwatch.StartNew();
            await _next(context);
            stopwatch.Stop();

            var request = context.Request;
            var response = context.Response;

            _logger.LogInformation("Request: {Method} {Path} responded with {StatusCode} in {ElapsedMilliseconds} ms",
                request.Method, request.Path, response.StatusCode, stopwatch.ElapsedMilliseconds);
        }
    }
}