package app.aws.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Setter
@Getter
@ToString
public class Trade {
    private String tradeId;
    private int quantity;
    private double price;
}