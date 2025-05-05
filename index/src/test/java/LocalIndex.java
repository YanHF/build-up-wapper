import com.ly.flight.xpgs.policy.model.IndexKey;
import lombok.Data;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.SplittableRandom;

@Data
public class LocalIndex implements IndexKey {

    private String dataId;

    private String name;

    private Integer gender;

    @Override
    public String getIndexKey() {
        return this.dataId;
    }

    @Override
    public byte[] getIndexKeyBytes() {
        return dataId.getBytes(StandardCharsets.UTF_8);
    }
}
