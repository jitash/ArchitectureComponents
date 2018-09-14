package cn.jitash.architecture.statistics;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
import android.databinding.ObservableBoolean;

import cn.jitash.architecture.data.source.TasksRepository;

/**
 * Created by jbs on 2018/9/14
 */
public class StatisticsViewModel extends AndroidViewModel {
    public final ObservableBoolean empty = new ObservableBoolean();
    private int mNumberOfActiveTasks = 0;
    private final Context mContext;
    private final TasksRepository mTasksRepository;

    public StatisticsViewModel(Application context, TasksRepository tasksRepository) {
        super(context);
        mContext = context;
        mTasksRepository = tasksRepository;
    }
    
    public void start(){}
    
    public void loadStatistics(){ 
    }
}
