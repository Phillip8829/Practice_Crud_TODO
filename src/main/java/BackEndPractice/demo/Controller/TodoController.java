package BackEndPractice.demo.Controller;

import BackEndPractice.demo.Model.TodoModel;
import BackEndPractice.demo.Repo.TodoRepo;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/items")
public class TodoController
{
        TodoRepo  todoRepo;

        //Constructor
    public TodoController(TodoRepo todoRepo){
        this.todoRepo = todoRepo;
    }


    //GET ALL Controller
    @GetMapping("")
    List<TodoModel> getAllTodo()
    {
        return this.todoRepo.findAll();
    }

    //POST / CREATE Todo Item
    @PostMapping
    ResponseEntity<TodoModel> postItem(@RequestBody TodoModel body)
    {
        return new ResponseEntity<>(this.todoRepo.save(body), HttpStatus.OK);
    }

    //UPDATE / PATCH
    @PatchMapping("{id}")
    ResponseEntity<TodoModel> patchTodo(@PathVariable Long id, @RequestBody Map<String, String> body)
    {
        //Is User Present
        if (this.todoRepo.findById(id).isPresent())
        {
            TodoModel holder = this.todoRepo.findById(id).get();
            for (Map.Entry<String, String> entry : body.entrySet())
            {
                switch (entry.getKey())
                {
                    case "Content" -> holder.setContent(entry.getValue());
                    case "Completed" -> holder.setCompleted(Boolean.parseBoolean(entry.getValue()));
                }
            }
            //Lets Now Save it
            this.todoRepo.save(holder);
            return new ResponseEntity<>(this.todoRepo.findById(id).get(), HttpStatus.OK);
        }
        //User Was Not Found
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }


    //DELETE ITEM
@DeleteMapping("{id}")
    ResponseEntity<TodoModel> deleteItem(@PathVariable Long id)
{
    //Exist
    if(this.todoRepo.findById(id).isPresent())
    {
        this.todoRepo.deleteById(id);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
    //Does Not Exist
    return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
}


}
