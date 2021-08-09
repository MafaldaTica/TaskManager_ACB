package com.company;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;

public class TaskManager implements Serializable {
    private final String name;
    private PriorityQueue<Process> process_Queue;

    public int getMaxProcessRunning() {
        return maxProcessRunning;
    }

    public void setMaxProcessRunning(int maxProcessRunning) {
        if (maxProcessRunning >= 1000) {
            //Defining an arbitrary maximum size of running processes since no clue of memory sizes.
            this.maxProcessRunning = 1000;
        }
        this.maxProcessRunning = maxProcessRunning;
    }

    private int maxProcessRunning;
    private boolean addingWithPriority;

    public TaskManager(String name, PriorityQueue<Process> processQueue, int maximumAllowedProcesses ) {
        this.name = name;
        this.process_Queue = processQueue;
        this.maxProcessRunning = maximumAllowedProcesses;
    }

    public String getName() {
        return name;
    }

    public PriorityQueue<Process> getProcess_Queue() {
        return process_Queue;
    }

    public void setProcess_Queue(PriorityQueue<Process> processQueue) {
        this.process_Queue = processQueue;
    }

    public boolean isAddingWithPriority() {
        return addingWithPriority;
    }

    public void setAddingWithPriority(boolean addingWithPriority) {
        this.addingWithPriority = addingWithPriority;
    }
    @Override
    public String toString() {
        return "The Process Queue is{" +
                "Name='" + name + '\'' +
                ", List Of Processes ='" + process_Queue.toString() + '\'' +
                '}';
    }

    public void KillAllProcesses() {
        try {
            if (!process_Queue.isEmpty()) {
                process_Queue.clear();
            }
        } catch (Exception e) {
            System.out.println("Could not kill the processes.");
        }
    }

    public int KillAllProcesses1() {
        int result = 0;
        try {
            if (!process_Queue.isEmpty()) {
                process_Queue.clear();
            }
        } catch (Exception e) {
            result = 1;
        }
        return result;
    }

    public ArrayList<Process> getArrayListFromQueue(PriorityQueue<Process> theQueue) {
        Process aProcess;
        Iterator<Process> iter1 = theQueue.iterator();
        ArrayList<Process> processArrayList = new ArrayList<>();

        while (iter1.hasNext()) {
            aProcess = (iter1.next());
            processArrayList.add(aProcess);
        }
        return processArrayList;
    }

     public void ListProcessesByPriority() {
        if (!process_Queue.isEmpty()) {
            ArrayList<Process> theProcessArrayList;
            theProcessArrayList = getArrayListFromQueue(process_Queue);

            theProcessArrayList.sort(new Comparator<Process>() {
                // Compare function based on the priority ordering from high to low, since low < medium < high.
                public int compare(Process p1, Process p2) {
                    return p1.getValueFromPrio()-p2.getValueFromPrio();
                }
            });
            for (Process theProcess : theProcessArrayList) System.out.println(theProcess.toString());
        } else {
            System.out.println("No process running.");
        }
    }

    public void ListProcessesByPID() {
        if (!process_Queue.isEmpty()) {
            ArrayList<Process> theProcessArrayList;
            theProcessArrayList = getArrayListFromQueue(process_Queue);

            theProcessArrayList.sort(new Comparator<Process>() {
                // Compare function based on an alphanumeric order of the processes identification.
                public int compare(Process p1, Process p2) {
                    return p1.getpId().compareTo(p2.getpId());
                }
            });
            for (Process theProcess : theProcessArrayList) System.out.println(theProcess.toString());
        } else {
            System.out.println("No process running.");
        }
    }

    public void ListProcessesByCreation() {
        // List all processes by creation time.

        if (!process_Queue.isEmpty()) {
            ArrayList<Process> theProcessArrayList;
            theProcessArrayList = getArrayListFromQueue(process_Queue);
            for (Process theProcess : theProcessArrayList) {
                System.out.println(theProcess.toString());
            }
        } else {
            System.out.println("No process running.");
        }
    }

    private Timestamp getTimestamp() {
        Date date = new Date();
        return new Timestamp(date.getTime());
    }
    public void AddProcess(String processName, String priority) {
        //Add a process

        int currProcessesRunning = process_Queue.size();
        Timestamp ts=getTimestamp();
        //System.out.println("Number of processes running: " + currProcessesRunning);
        Process newProcess = new Process(processName,priority,ts);

        if (currProcessesRunning==this.getMaxProcessRunning()) {
            if (isAddingWithPriority()) {
                Process aProcessToRemove = searchProcessWithLowerPriority(priority);
                // Remove lower priority process.
                System.out.println("*** Ready to remove ***");
                if (aProcessToRemove!=null) {
                    process_Queue.remove(aProcessToRemove);
                    process_Queue.add(newProcess);
                } else {
                    System.out.println("No process to remove, limit of running processes reached: "+ this.getMaxProcessRunning());
                }
            } else
                System.out.println("Limit of running processes reached: "+ this.getMaxProcessRunning());
        } else {
            process_Queue.add(newProcess);
            System.out.println("Just added -->"+ newProcess.toString());
        }
        System.out.println("Number of processes running: " + process_Queue.size());
    }

    private Process searchProcessWithLowerPriority(String priority) {
        ArrayList<Process> theListOfProcesses = new ArrayList<>();
        Process aProcess = null;

        if (!process_Queue.isEmpty()) {
            theListOfProcesses = getArrayListFromQueue(process_Queue);

            Collections.sort(theListOfProcesses,new Comparator<Process>() {
                        // Compare function based on the priority ordering from high to low, since low < medium < high.
                        public int compare(Process p1, Process p2) {
                            return p1.getValueFromPrio()-p2.getValueFromPrio();
                        }
                });

            //System.out.println("-List by priority--"+ theListOfProcesses);

            int a=0;
            int b=0;
            int i=theListOfProcesses.size();

            ArrayList<Process> tempProcsList = new ArrayList<Process>();

            for (Process p: theListOfProcesses) {
                a = p.getPriority().getValueFromName(priority);
                b = p.getValueFromPrio();
                if ((a < b) || (i == 0)) {
                    // Form a new array only with the processes with lower priority to select the one that shall be removed.
                    if ((a - b == -2) && (i != 0)) {
                        tempProcsList.add(p);
                    }
                }
                i -= 1;
                continue;
            }
            // Order list by timestamp and delete last.
            Collections.sort(tempProcsList,new Comparator<Process>() {
                // Compare function based on the priority ordering from high to low, since low < medium < high.
                public int compare(Process p1, Process p2) {
                    //System.out.println("PID: "+ p1.getpId().substring(0,5) +"p1 Prio val:"+p1.getValueFromPrio()+ " PID: "+ p2.getpId().substring(0,5) +"p2 Prio val:"+ p2.getValueFromPrio());
                    return p1.compareTo(p2);
                }
            });
            //System.out.println("tempProcsList --"+ tempProcsList.toString());

            //int last=tempProcsList.size()-1;
            if (!tempProcsList.isEmpty()) {
                aProcess = tempProcsList.get(0);
                System.out.println("Process to remove: "+ aProcess.toString());
            }
        }
        return(aProcess);
    }

      public void KillProcess(String pid) {
        // Kill the process with the provided pid

        if (process_Queue.isEmpty()) {
            System.out.println("No current process running");
        } else {
            Iterator<Process> iter1 = process_Queue.iterator();
            Process aProcess;
            Process toRemoveProcess=null;

            do {
                aProcess = (iter1.next());
                if (pid.equals(aProcess.getpId())) {
                    toRemoveProcess=aProcess;
                }
            } while (iter1.hasNext() && toRemoveProcess==null);

            if (toRemoveProcess!=null) {
                System.out.println("Process found and removed: "+ toRemoveProcess.getpId());
                process_Queue.remove(toRemoveProcess);

            } else {
                System.out.println("No process found with id: "+pid);
            }
        }
    }

    public void KillAllProcesses(Priority withThisPrio) {
        int removedProcesses=0;
        int i =0;
        int currentNumberOfProcesses = process_Queue.size();

        Iterator<Process> iter1 = process_Queue.iterator();
        boolean allScanned=false;
        Process aProcess;
        Process toRemoveProcess=null;

        while (!allScanned && currentNumberOfProcesses>0) {
            do {
                aProcess = (iter1.next());
                if (withThisPrio.equals(aProcess.getPriority())) {
                    toRemoveProcess = aProcess;
                }
                i++;
            } while (iter1.hasNext() && toRemoveProcess == null);

            if (toRemoveProcess != null) {
                process_Queue.remove(toRemoveProcess);
                removedProcesses++;
                toRemoveProcess=null;
            } else if (i <= currentNumberOfProcesses) {
                allScanned = true;
            }
            iter1 = process_Queue.iterator();
            currentNumberOfProcesses = process_Queue.size();
            i = 0;

        }
        System.out.println("Removed "+ removedProcesses + " processes with priority=" + withThisPrio);
    }

}
