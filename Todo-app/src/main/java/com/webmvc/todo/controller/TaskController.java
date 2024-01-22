package com.webmvc.todo.controller;

import com.webmvc.todo.model.Task;
import com.webmvc.todo.repository.TaskRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@Controller

public class TaskController {

    private final TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @GetMapping("/list")
    public String listTasks(Model model) {
        List<Task> tasks = taskRepository.findAll();
        model.addAttribute("tasks", tasks);
        return "list";
    }

    @GetMapping("/new")
    public String newTaskForm(Model model) {
        model.addAttribute("task", new Task());
        return "new";
    }

    @PostMapping("/save")
    public String saveTask(@Valid @ModelAttribute Task task, BindingResult bindingResult) {

        System.out.println(task);
        if(bindingResult.hasErrors())
        {
            return "new";
        }
        taskRepository.save(task);
        return "redirect:/list";
    }

    @GetMapping("/edit/{id}")
    public String editTaskForm(@PathVariable Long id, Model model) {
        Optional<Task> task = taskRepository.findById(id);
        task.ifPresent(value -> model.addAttribute("task", value));
        return "edit";
    }

    @PostMapping("/update/{id}")
    public String updateTask(@PathVariable Long id,@Valid @ModelAttribute Task updatedTask,BindingResult bindingResult) {
       if(bindingResult.hasErrors())
       {
           return "edit";
       }
        Optional<Task> task = taskRepository.findById(id);
        task.ifPresent(value -> {
            value.setTaskName(updatedTask.getTaskName());
            value.setDescription(updatedTask.getDescription());
            value.setDueDate(updatedTask.getDueDate());
            taskRepository.save(value);
        });
        return "redirect:/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskRepository.deleteById(id);
        return "redirect:/list";
    }


    @PostMapping("/complete/{id}")
    public String markAsCompleted(@PathVariable Long id) {
        Task taskToComplete = taskRepository.findById(id).orElse(null); // Fetch the task by its ID
        if (taskToComplete != null) {
            taskToComplete.setCompleted(true); // Set the task as completed
            taskRepository.save(taskToComplete); // Save the updated task
        }
        return "redirect:/list";
    }

}
