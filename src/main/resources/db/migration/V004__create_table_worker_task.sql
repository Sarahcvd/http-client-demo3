create table worker_task (
    task_id int references task (id),
    worker_id int not null,
    foreign key (worker_id) references worker,
    primary key (task_id, worker_id)
)