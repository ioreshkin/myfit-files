package center.myfit.enums;

/** Енам размеров изображений. */
public enum ImageSize {
  ORIGINAL("original"),
  MOBILE("mobile"),
  DESKTOP("desktop");

  private String postfix;

  ImageSize(String postfix) {
    this.postfix = postfix;
  }

  public String getPostfix() {
    return postfix;
  }
}
