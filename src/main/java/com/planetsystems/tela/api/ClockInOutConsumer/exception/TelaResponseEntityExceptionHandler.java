package com.planetsystems.tela.api.ClockInOutConsumer.exception;


import com.planetsystems.tela.api.ClockInOutConsumer.dto.MQResponseDto;
import com.planetsystems.tela.api.ClockInOutConsumer.dto.SystemErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class TelaResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) throws Exception {
        MQResponseDto<SystemErrorDTO> exceptionResponseDTO = new MQResponseDto<SystemErrorDTO>();
   	exceptionResponseDTO.setData(new SystemErrorDTO(ex.getMessage() , HttpStatus.INTERNAL_SERVER_ERROR.value()));
       ex.printStackTrace();
       return new ResponseEntity<>(exceptionResponseDTO , HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(TelaNotFoundException.class)
    public final ResponseEntity<Object> handleNotFoundException(TelaNotFoundException ex, WebRequest request) throws Exception {
        MQResponseDto<SystemErrorDTO> exceptionResponseDTO = new MQResponseDto<SystemErrorDTO>();
       	exceptionResponseDTO.setData(new SystemErrorDTO(ex.getMessage() ,  HttpStatus.NOT_FOUND.value()));
        return new ResponseEntity<>(exceptionResponseDTO , HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public final ResponseEntity<Object> handleInvalidTokenException(InvalidTokenException ex, WebRequest request) throws Exception {
        MQResponseDto<SystemErrorDTO> exceptionResponseDTO = new MQResponseDto<SystemErrorDTO>();
       	exceptionResponseDTO.setData(new SystemErrorDTO(ex.getMessage() ,  HttpStatus.FORBIDDEN.value()));
        return new ResponseEntity<>(exceptionResponseDTO , HttpStatus.FORBIDDEN);
    }
    



    @ExceptionHandler(AlreadyExitsException.class)
    public final ResponseEntity<Object> handleUserNameAlreadyUsedException(AlreadyExitsException ex, WebRequest request) throws Exception {
        MQResponseDto<SystemErrorDTO> exceptionResponseDTO = new MQResponseDto<SystemErrorDTO>();
     	exceptionResponseDTO.setData(new SystemErrorDTO(ex.getMessage() ,  HttpStatus.CONFLICT.value()));
        return new ResponseEntity<>(exceptionResponseDTO , HttpStatus.CONFLICT);
    }


    @ExceptionHandler(InValidCredentialsException.class)
    public final ResponseEntity<Object> handleInValidCredentialsException(InValidCredentialsException ex, WebRequest request) throws Exception {
        MQResponseDto<SystemErrorDTO> exceptionResponseDTO = new MQResponseDto<SystemErrorDTO>();
  	exceptionResponseDTO.setData(new SystemErrorDTO(ex.getMessage() ,  HttpStatus.UNAUTHORIZED.value()));
        return new ResponseEntity<>(exceptionResponseDTO , HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(HttpClientErrorException.BadRequest.class)
    public final ResponseEntity<Object> handleInBadRequestException(HttpClientErrorException.BadRequest ex, WebRequest request) throws Exception {
        MQResponseDto<SystemErrorDTO> exceptionResponseDTO = new MQResponseDto<SystemErrorDTO>();
        exceptionResponseDTO.setData(new SystemErrorDTO(ex.getMessage() ,  HttpStatus.BAD_REQUEST.value()));
        return new ResponseEntity<>(exceptionResponseDTO , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingDataException.class)
    public final ResponseEntity<Object> handleMissingDataException(MissingDataException ex, WebRequest request) throws Exception {
        MQResponseDto<SystemErrorDTO> exceptionResponseDTO = new MQResponseDto<SystemErrorDTO>();
        exceptionResponseDTO.setData(new SystemErrorDTO(ex.getMessage() ,  HttpStatus.BAD_REQUEST.value()));
        return new ResponseEntity<>(exceptionResponseDTO , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpClientErrorException.NotFound.class)
    public final ResponseEntity<Object> handleNotFoundException(MissingDataException ex, WebRequest request) throws Exception {
        MQResponseDto<SystemErrorDTO> exceptionResponseDTO = new MQResponseDto<SystemErrorDTO>();
        exceptionResponseDTO.setData(new SystemErrorDTO(ex.getMessage() ,  HttpStatus.NOT_FOUND.value()));
        return new ResponseEntity<>(exceptionResponseDTO , HttpStatus.NOT_FOUND);
    }


}
