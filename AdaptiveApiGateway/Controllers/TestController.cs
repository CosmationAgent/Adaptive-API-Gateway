using Microsoft.AspNetCore.Mvc;

namespace AdaptiveApiGateway.Controllers
{
    [ApiController]
    [Route("api/test")]
    public class TestController : ControllerBase
    {
        [HttpGet]
        public IActionResult Get()
        {
            return Ok("Hello from TestController!");
        }
    }
}