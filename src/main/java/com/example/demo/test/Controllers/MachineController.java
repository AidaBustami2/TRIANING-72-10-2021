package com.example.demo.test.Controllers;
import com.example.demo.test.Models.Machine;
import com.example.demo.test.Repositories.MachineRepository;
import com.example.demo.test.Repositories.TaskRepository;
import com.example.demo.test.Repositories.ThreadRepository;
import com.example.demo.test.Repositories.UserRepository;
import com.example.demo.test.Services.Job;
import com.example.demo.test.Models.Task;
import com.example.demo.test.Models.Thread;
import com.example.demo.test.Models.User;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;



@RestController


public class MachineController {
    @Autowired
    MachineRepository machineRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ThreadRepository threadRepository;


    public MachineController(MachineRepository machineRepository, UserRepository userRepository, Job job, TaskRepository taskRepository) {
        this.machineRepository = machineRepository;
        this.userRepository = userRepository;
        this.job = job;
        this.taskRepository = taskRepository;
    }

    public MachineController() {
    }

    @Autowired
    Job job;
    @Autowired
    TaskRepository taskRepository;

    @PostMapping("/update")
    public String f1(@ModelAttribute Machine m) {
        machineRepository.save(m);
        return "done updating";
    }

    @PostMapping("/create/{name}/{ip}/{location}/{user_id}")
    public String f2(@PathVariable String ip, @PathVariable String name, @PathVariable String location, @PathVariable Long user_id) {
        Machine machine = new Machine(name, ip, location);


        Optional<User> user = userRepository.findById(user_id);

        if (user.isPresent()) {

            machine.getUserList().add(user.get());


            user.get().getMachineByUser().add(machine);
            machineRepository.save(machine);
            userRepository.save(user.get());
            return "done creating";

        }
        return "user does not exist";


    }

    @PostMapping("/create")
    public String f3(@RequestBody ObjectNode objectNode) {


        Machine machine = new Machine(objectNode.get("name").asText(), objectNode.get("ip").asText(), objectNode.get("location").asText());
        machineRepository.save(machine);
        return "done creating";


    }


    // to work with @ModelAttribute use as body  x www foramt key / values
    // to work with  @RequestBody  use as body as as raw json and  m.toString(); is correct

    @PutMapping("/update2")
    public String f5(@RequestBody ObjectNode objectNode) {


        Optional<Machine> optional = machineRepository.findById(objectNode.get("id").asLong());
        Machine machine = null;
        if (optional.isPresent()) {
            machine = optional.get();
            //  machine.setIP(objectNode.get("IP").asText());
            //  machine.setLocation(objectNode.get("location").asText());
            machine.setName(objectNode.get("name").asText());
            machineRepository.save(machine);
            return "updated !!!";

        } else
            return " machine id not in database !!!";


    }

    @PutMapping("/delete_a_machine/{machineId}")
    public String f6(@PathVariable long machineId) {
        Optional<Machine> optional = machineRepository.findById(machineId);


        if (optional.isPresent()) {

            job.deleteUserMachineReference(machineId);
            job.deleteTasks(optional);
            machineRepository.delete(optional.get());       //  cascade delete
            return "deleted" ;


        }
        return "machine does not exist" ;
    }


    @PutMapping("/add_task/{machineId}/{userId}")
    public String f8(@ModelAttribute Task task, @PathVariable Long machineId, @PathVariable Long userId) {
        Optional<Task> taskOptional = taskRepository.findById(task.getTaskId());
        Task task1 = task;

        if (!(taskOptional.isPresent())) {
            task1 = new Task();
            task1.setName(task.getName());

            if (taskRepository.count() ==0) {
                Thread threads[] = new Thread[3];
                threads[0] = new Thread();
                threads[1] = new Thread();
                threads[2] = new Thread();
                threads[0].setBusy(false);
                threads[0].setTaskId(null);
                threads[1].setBusy(false);
                threads[1].setTaskId(null);
                threads[2].setBusy(false);
                threads[2].setTaskId(null);
                threadRepository.save(threads[0]);
                threadRepository.save(threads[1]);
                threadRepository.save(threads[2]);

            }
            taskRepository.save(task1);

        }

        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.getTaskByUser().add(task1);
            task.getUserList().add(user);
            taskRepository.save(task1);
            userRepository.save(user);
            Optional<Machine> optionalMachine = machineRepository.findById(machineId);
            if (optionalMachine.isPresent()) {
                optionalMachine.get().getUserList().add(user);
                user.getMachineByUser().add(optionalMachine.get());
                job.addTask(task1, machineId);
                return "task added " ;
            }
            else
                return "machine does not exist" ;



        }
        else {
            return "user does not exist";

        }



    }


}