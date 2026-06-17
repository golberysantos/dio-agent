package com.santander;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AppTest {
    
    @Test
    public void testSomar() {
        App app = new App();
        int resultado = app.somar(5, 5);
        
        // Verifica se a soma de 5 + 5 é igual a 10
        assertEquals(10, resultado, "A soma deve ser 10");
    }
}
