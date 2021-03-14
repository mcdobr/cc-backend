create table user_transaction
(
    id uuid default gen_random_uuid() not null
        constraint transaction_pk
            primary key,
    sum numeric not null,
    sender uuid not null,
    receiver uuid not null,
    created_at timestamp with time zone not null,
    last_modified_at timestamp with time zone not null,
    description varchar
);
