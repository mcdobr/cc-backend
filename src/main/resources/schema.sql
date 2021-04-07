drop table if exists transaction_history;

create table if not exists transaction_history
(
    id uuid default gen_random_uuid() not null
        constraint transaction_pk
            primary key,
    sum numeric not null,
    other_party varchar not null,
    created_at timestamp with time zone not null,
    last_modified_at timestamp with time zone not null,
    transaction_type varchar not null,
    description varchar not null
);
