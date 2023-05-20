public class Job {
    private final int jobId;
    private final String jobName;
    private final String startDate;
    private final String endDate;
    public Job(int jobId,String startDate,String endDate){
        Database database = new Database();
        this.jobId = jobId;
        this.jobName = database.getJob(jobId);
        this.startDate = startDate;
        this.endDate = endDate;
        database.close();
    }
    public Job(int jobId,String jobName){
        this.jobId = jobId;
        this.jobName = jobName;
        this.startDate = null;
        this.endDate = null;
    }
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
    @Override
    public String toString(){
        return String.format("Job ID: %d, Job Name: %s, Start Date: %s, End Date: %s",jobId,jobName,startDate,endDate);
    }
}
