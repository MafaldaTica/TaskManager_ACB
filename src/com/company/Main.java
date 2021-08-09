package com.company;

import java.util.PriorityQueue;
import java.util.Scanner;

public class Main {

    private static void showHelp(String applicationName) {
        System.out.println("=================================================================================");
        System.out.println("Program Name: " + applicationName);
        System.out.println("Commands are case sensitive.");
        System.out.println("help: To display this text again.");
        System.out.println("quit: To stop the task manager.");
        System.out.println("ms:number      -> To define a maximum number of processes allowed.");
        System.out.println("a:priority     -> To add a process enter the priority after the a. Valid priorities are: low,medium,high");
        System.out.println("a:pid:priority -> To add a process enter with a specified pid and a priority. Valid priorities are: low,medium,high");
        System.out.println("prio           -> To use priority for adding a process.");
        System.out.println("noprio         -> To not use the priority for adding a process");
        System.out.println("k:pid          -> To kill a process with process id = pid.");
        System.out.println("lc, lp or li   -> To list a process by (c)reation time, (p)riority or (i)d.");
        System.out.println("ka             -> To kill all processes.");
        System.out.println("ka:low         -> To kill all process with low priority.");
        System.out.println("ka:medium      -> To kill all process with medium priority.");
        System.out.println("ka:high        -> To kill all process with the given priority.");
        System.out.println("=================================================================================");
    }
    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        // Maximum number of processes
        int maxRunningProcesses = 4;

        String userInput;
        String processID;
        String processPriority;
        TaskManager myTaskManager = new TaskManager("MyTaskManager",new PriorityQueue<>(), maxRunningProcesses);
        myTaskManager.setAddingWithPriority(false);

        showHelp(myTaskManager.getName());
        do {
            userInput = input.nextLine();
            switch (userInput)
            {
                case "help":
                    showHelp(myTaskManager.getName());
                    break;
                case "ka":
                    System.out.println("Killing all processes.");
                    System.out.println(myTaskManager.KillAllProcesses()==0?"All processes killed" : "Problem during killing of processes.");
                    myTaskManager.ListProcessesByCreation();
                    break;
                case "lc":
                    System.out.println("List processes by creation time.");
                    myTaskManager.ListProcessesByCreation();
                    break;
                case "lp":
                    System.out.println("List processes by priority.");
                    myTaskManager.ListProcessesByPriority();
                    break;
                case "li":
                    System.out.println("List processes by pid.");
                    myTaskManager.ListProcessesByPID();
                    break;
                case "ka:low":
                    System.out.println("Killing all low priority processes.");
                    myTaskManager.KillAllProcesses(Priority.LOW);
                    break;
                case "ka:medium":
                    System.out.println("Killing all medium priority processes.");
                    myTaskManager.KillAllProcesses(Priority.MEDIUM);
                    break;
                case "ka:high":
                    System.out.println("Killing all high priority processes.");
                    myTaskManager.KillAllProcesses(Priority.HIGH);
                    break;
                case "prio":
                    System.out.println("Setting priority use for adding");
                    myTaskManager.setAddingWithPriority(true);
                    break;
                case "noprio":
                    System.out.println("Removing priority use for adding.");
                    myTaskManager.setAddingWithPriority(false);
                    break;
                default:
                    System.out.println("Command is: " + userInput);
                    String[] commands = userInput.split(":");
                    switch (commands[0]) {
                        case "a":
                            if (commands.length == 2) {
                                processPriority = commands[1];
                                if (processPriority.length()>0) {
                                    // Any priority not specified will be taken as low.
                                    myTaskManager.AddProcess(processPriority);
                                } else {
                                    System.out.println("Invalid input. Priority cannot be null: " + userInput);
                                }
                            } else {
                                System.out.println("Invalid input: " + userInput);
                            }
                            break;
                        case "k":
                            if (commands.length == 2) {
                                processID = commands[1];
                                if (processID.length()>0) {
                                    myTaskManager.KillProcess(processID);
                                } else {
                                    System.out.println("Invalid input, the process id must be specified: "+userInput);
                                }
                            } else {
                                System.out.println("Invalid input: " + userInput);
                            }
                            break;
                        case "ms":
                            int i = maxRunningProcesses;
                            if (commands.length == 2) {
                                try {
                                    i = Integer.parseInt(commands[1]);
                                } catch (Exception e) {
                                    System.out.println("Invalid number of running processes: " + userInput);
                                    System.out.println("Using default value: " + i);
                                }
                                myTaskManager.setMaxProcessRunning(i);
                            } else {
                                System.out.println("Invalid input: " + userInput);
                            }
                            break;
                        default:
                    }
            }

        } while (!userInput.equalsIgnoreCase("quit"));

        System.out.println("=======THE END ========");

    }

}
