package dto;

import lombok.Data;

@Data
public class Support {

    private String url;
    private String text;

    public Support() {
        super();
    }
}
