package juniter.core.validation.meta;

import javax.validation.*;
import java.lang.annotation.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;

public class ClassLevelConstraintExample {
    private static final Validator validator;

    static {
        Configuration<?> config = Validation.byDefaultProvider().configure();
        ValidatorFactory factory = config.buildValidatorFactory();
        validator = factory.getValidator();
        factory.close();
    }

    @ValidOrder
    public static class Order {
        private final BigDecimal price;
        private final BigDecimal quantity;

        public Order (BigDecimal price, BigDecimal quantity) {
            this.price = price;
            this.quantity = quantity;
        }

        public BigDecimal getPrice () {
            return price;
        }

        public BigDecimal getQuantity () {
            return quantity;
        }

        public BigDecimal getTotalPrice () {
            return (price != null && quantity != null ?
                    price.multiply(quantity) : BigDecimal.ZERO)
                    .setScale(2, RoundingMode.CEILING);
        }
    }

    public static void main (String[] args) throws NoSuchMethodException {
        Order order = new Order(new BigDecimal(4.5), new BigDecimal(9));

        Set<ConstraintViolation<Order>> constraintViolations = validator.validate(order);

        if (constraintViolations.size() > 0) {
            constraintViolations.stream().forEach(
                    ClassLevelConstraintExample::printError);
        } else {
            //proceed using order
            System.out.println(order);
        }
    }

    private static void printError (
            ConstraintViolation<Order> violation) {
        System.out.println(violation.getPropertyPath() + " " + violation.getMessage());
    }

    @Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = OrderValidator.class)
    @Documented
    public static @interface ValidOrder {
        String message () default "total price must be 50 or greater for online order. " +
                "Found: ${validatedValue.totalPrice}";

        Class<?>[] groups () default {};

        Class<? extends Payload>[] payload () default {};
    }

    public static class OrderValidator implements ConstraintValidator<ValidOrder, Order> {
        @Override
        public void initialize (ValidOrder constraintAnnotation) {
        }

        @Override
        public boolean isValid (Order order, ConstraintValidatorContext context) {
            if (order.getPrice() == null || order.getQuantity() == null) {
                return false;
            }
            return order.getTotalPrice()
                    .compareTo(new BigDecimal(50)) >= 0;

        }
    }
}