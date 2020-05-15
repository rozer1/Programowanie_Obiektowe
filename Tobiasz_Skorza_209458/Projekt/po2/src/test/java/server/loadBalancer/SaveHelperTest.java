package server.loadBalancer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

import server.SaveHelper;
import server.SaveFile;
import server.TaskInfo;

import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.verifyNew;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest(fullyQualifiedNames = "server.*")
public class SaveHelperTest
    {
    Path path;
    SaveHelper saveHelper;
    SaveFile task;
    TaskInfo dataForTask;

    @Before
    public void setUp() throws Exception
        {
        dataForTask = new TaskInfo("user", "file", null);

        path = Paths.get(System.getProperty("user.home")).resolve("test");

        saveHelper = new SaveHelper(new ArrayList<Path>()
            {{
            add(path);
            }});

        task = mock(SaveFile.class);
        whenNew(SaveFile.class).withArguments(any(LinkedBlockingQueue.class), any(PriorityBlockingQueue.class)).thenReturn(task);
        }

    @Test
    public void shouldCreateOneSaveHandler() throws Exception
        {
        saveHelper.saveFile(dataForTask);
        verifyNew(SaveFile.class).withArguments(any(LinkedBlockingQueue.class),
                any(PriorityBlockingQueue.class));
        }

    @Test
    public void shouldReturn2AsPriorityBecauseUserSaveFileTwoTimes()
        {
        saveHelper.saveFile(dataForTask);
        saveHelper.saveFile(dataForTask);
        Assert.assertThat(dataForTask.getPriority(), is(2));
        }

    @Test
    public void shouldReturn0AsPriority()
        {
        saveHelper.saveFile(dataForTask);
        Assert.assertThat(dataForTask.getPriority(), is(1));
        }

    @Test
    public void shouldReturn0AsCountBecauseOfSaveFileFunction()
        {
        saveHelper.getCount("user");
        saveHelper.getCount("user");
        saveHelper.saveFile(dataForTask);
        Integer count = saveHelper.getCount("user");
        Assert.assertThat(count, is(0));
        }

    @Test
    public void shouldReturn2AsCount()
        {
        saveHelper.getCount("user");
        saveHelper.getCount("user");
        Integer count = saveHelper.getCount("user");
        Assert.assertThat(count, is(2));
        }

    @Test
    public void shouldReturn1AsCount()
        {
        saveHelper.getCount("user");
        saveHelper.getCount("user2");
        Integer count = saveHelper.getCount("user");
        Integer count2 = saveHelper.getCount("user2");
        Assert.assertThat(count, is(1));
        Assert.assertThat(count2, is(1));
        }

    @Test
    public void shouldReturnTrueBecauseUser1HasBiggerPriority()
        {
        TaskInfo data1 = new TaskInfo("user", "file", null);
        TaskInfo data2 = new TaskInfo("user", "file", null);
        TaskInfo data3 = new TaskInfo("user1", "file", null);

        saveHelper.saveFile(data1);
        saveHelper.saveFile(data2);
        saveHelper.saveFile(data3);

        Assert.assertTrue(data3.compareTo(data2) > 0);
        }

    @Test
    public void shouldReturnFalseBecauseUser1DoesntHaveBiggerPriority()
        {
        TaskInfo data1 = new TaskInfo("user", "file", null);
        TaskInfo data2 = new TaskInfo("user", "file", null);
        TaskInfo data3 = new TaskInfo("user1", "file", null);
        TaskInfo data4 = new TaskInfo("user1", "file", null);
        TaskInfo data5 = new TaskInfo("user1", "file", null);

        saveHelper.saveFile(data1);
        saveHelper.saveFile(data2);
        saveHelper.saveFile(data3);
        saveHelper.saveFile(data4);
        saveHelper.saveFile(data5);

        Assert.assertTrue(data5.compareTo(data2) < 0);
        }
    }
