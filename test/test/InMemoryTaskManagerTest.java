package test;

import manager.InMemoryTaskManager;

class InMemoryTaskManagerTest extends test.TaskManagerTest<InMemoryTaskManager> {

    @Override
    public InMemoryTaskManager createManager() {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        return inMemoryTaskManager;
    }
}