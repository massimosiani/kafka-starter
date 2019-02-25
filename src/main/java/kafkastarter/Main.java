package kafkastarter;

public class Main {

    public static void main(String[] args) throws Exception {
        Producer.produce(Producer.producer());
        Consumer.consume(Consumer.consumer());
    }
}
