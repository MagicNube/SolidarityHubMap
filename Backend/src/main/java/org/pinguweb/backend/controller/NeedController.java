package org.pinguweb.backend.controller;

import org.pinguweb.DTO.NeedDTO;
import org.pinguweb.backend.DTO.BackendDTOFactory;
import org.pinguweb.backend.DTO.ModelDTOFactory;
import org.pinguweb.backend.controller.common.ServerException;
import org.pinguweb.backend.model.Need;
import org.pinguweb.backend.model.Need;
import org.pinguweb.backend.repository.NeedRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class NeedController {

    @Autowired
    NeedRepository repository;

    @Async
    @GetMapping("/need")
    public CompletableFuture<ResponseEntity<List<NeedDTO>>> getAll(){
        if (ServerException.isServerClosed(repository)){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}
        BackendDTOFactory factory = new BackendDTOFactory();

        List<NeedDTO> needs = repository.findAll().stream().map(factory::createNeedDTO).collect(Collectors.toList());

        return CompletableFuture.completedFuture(ResponseEntity.ok(needs));
    }

    @Async
    @GetMapping("/need/{id}")
    public CompletableFuture<ResponseEntity<NeedDTO>> getNeed(@PathVariable Integer id) {
        if (ServerException.isServerClosed(repository)){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        BackendDTOFactory factory = new BackendDTOFactory();
        if (repository.existsById(id)) {
            return CompletableFuture.completedFuture(ResponseEntity.ok(factory.createNeedDTO(repository.getReferenceById(id))));
        }
        else {
            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        }
    }

    @Async
    @PostMapping("/need")
    public CompletableFuture<ResponseEntity<NeedDTO>> addNeed(@RequestBody NeedDTO need) {
        if (ServerException.isServerClosed(repository)){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        ModelDTOFactory factory = new ModelDTOFactory();
        BackendDTOFactory dtoFactory = new BackendDTOFactory();

        return CompletableFuture.completedFuture(ResponseEntity.ok(dtoFactory.createNeedDTO(repository.save(factory.createFromDTO(need)))));
    }

    @Async
    @DeleteMapping("/need/{id}")
    public CompletableFuture<ResponseEntity<Void>> deleteNeed(@PathVariable int id) {
        if (ServerException.isServerClosed(repository)){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        if (repository.existsById(id)) {
            repository.deleteById(id);
            return CompletableFuture.completedFuture(ResponseEntity.ok().build());
        }
        else {
            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        }
    }

    @Async
    @PutMapping("/need")
    public CompletableFuture<ResponseEntity<NeedDTO>> updateNeed(@RequestBody NeedDTO need) {
        if (ServerException.isServerClosed(repository)){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        if (repository.existsById(need.getId())) {
            ModelDTOFactory factory = new ModelDTOFactory();
            BackendDTOFactory dtoFactory = new BackendDTOFactory();

            return CompletableFuture.completedFuture(ResponseEntity.ok(dtoFactory.createNeedDTO(repository.save(factory.createFromDTO(need)))));
        }
        else {
            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        }
    }
}
