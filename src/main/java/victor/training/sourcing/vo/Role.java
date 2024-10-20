package victor.training.sourcing.vo;

public record Role(String roleString) {
  // check it follows the pattern "app:role"
  public Role {
    if (!roleString.matches("[a-z]+:[a-z]+")) {
      throw new IllegalArgumentException("Invalid role format: " + roleString);
    }
  }
  public String app() {
    return roleString.split(":")[0];
  }
  public String role() {
    return roleString.split(":")[1];
  }
}
