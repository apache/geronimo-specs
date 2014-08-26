package javax.mail;

/**
 * A {@link javax.activation.DataSource DataSource} that also implements
 * EncodingAware may specify the Content-Transfer-Encoding
 * to use for its data.  Valid Content-Transfer-Encoding values specified
 * by RFC 2045 are "7bit", "8bit", "quoted-printable", "base64", and "binary".
 * 
 * For example, a {@link javax.activation.FileDataSource FileDataSource}
 * could be created that forces all files to be base64 encoded: 
 * 
 *  public class Base64FileDataSource extends FileDataSource
 *                  implements EncodingAware {
 *  public Base64FileDataSource(File file) {
 *      super(file);
 *  }
 *
 *  // implements EncodingAware.getEncoding()
 *  public String getEncoding() {
 *      return "base64";
 *  }
 *  }
 * 

 *
 * @since   JavaMail 1.5
 */

public interface EncodingAware {

    /**
     * Return the MIME Content-Transfer-Encoding to use for this data,
     * or null to indicate that an appropriate value should be chosen
     * by the caller.
     *
     * @return      the Content-Transfer-Encoding value, or null
     */
    public String getEncoding();
}
