public class Job {
    private final int jobId;
    private final String jobName;
    private final String startDate;
    private final String endDate;
    public Job(int jobId,String jobName,String startDate,String endDate){
        this.jobId = jobId;
        this.jobName = jobName;
        this.startDate = startDate;
        this.endDate = endDate;
    }
    public int getJobId(){
        return jobId;
    }
    public String getJobName(){
        return jobName;
    }
    public String getStartDate(){
        return startDate;
    }
    public String getEndDate(){
        return endDate;
    }
}
