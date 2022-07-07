
create table if not exists status
(
    id   bigserial      not null,
    name varchar(15) not null,
    primary key (id)
);

insert into status (id, name)
values (1, 'confirmado'),
       (2, 'borrador');