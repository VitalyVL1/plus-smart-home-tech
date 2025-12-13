package ru.practicum.client.shopping_store;

import org.springframework.stereotype.Component;
import ru.practicum.exception.BaseErrorDecoder;

/**
 * Декодер ошибок для Feign клиента магазина.
 */
@Component
public class ShoppingStoreErrorDecoder extends BaseErrorDecoder {
}