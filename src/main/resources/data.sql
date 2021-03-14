with targeted_users as (select uuid('4032b7ea-9061-4b9d-b436-ecfc78eb15ad') as first_user, uuid('015da977-69c3-4f53-8beb-2b3845ffbde5') as second_user)
insert into user_transaction(id, sum, sender, receiver, created_at, last_modified_at, description)
    select gen_random_uuid(), 100, targeted_users.first_user, targeted_users.second_user, now(), now(), 'Example transaction'
    from targeted_users;