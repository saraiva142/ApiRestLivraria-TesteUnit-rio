package apiTeste.livraria.service.exception;

public class EntityNotFound extends RuntimeException{
    public EntityNotFound(String msg) {
        super(msg);
    }
}
