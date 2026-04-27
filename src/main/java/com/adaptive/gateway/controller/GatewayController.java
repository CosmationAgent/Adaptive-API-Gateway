@RestController
@RequestMapping("/api/gateway")

public class GatewayController {

    @GetMapping("/process")
    public ResponseEntity<String> getGatewayStatus() {
        String status = gatewayService.getStatus();
        return ResponseEntity.ok(status);
    }

    @PostMapping("/configure")
    public ResponseEntity<String> configureGateway(@RequestBody GatewayConfig config) {
        boolean success = gatewayService.configure(config);
        if (success) {
            return ResponseEntity.ok("Gateway configured successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to configure gateway.");
        }
    }
}