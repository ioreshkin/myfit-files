package center.myfit.enums;

/** Енам размеров изображений. */
public enum ImageSize {
  ORIGINAL("o"),
  MOBILE("m"),
  DESKTOP("d");

  private String postfix;

  ImageSize(String postfix) {
    this.postfix = postfix;
  }

  public String getPostfix() {
    return postfix;
  }
}
