INSERT INTO public.organization (id,created_at,created_by,updated_at,updated_by,"version",address,description,name,logo) VALUES
    ('eef0cf24d249404f892f08ccf67bef48','2025-03-17 01:20:10.328','SYSTEM','2025-03-17 01:20:10.328','SYSTEM',0,'Jakarta','PamdesaKu Test','PamdesaKu',NULL);

-- password = 123456
INSERT INTO public.users (id,created_at,created_by,updated_at,updated_by,"version",active,address,email,full_name,"password",phone_number,user_role,organization_id) VALUES
    ('b70a853f73e94094a5b0b82ab874b53e','2025-03-17 01:21:42.470','SYSTEM','2025-03-17 01:21:42.470','SYSTEM',0,true,'Jakarta Selatan','admin@mail.com','Super User','$2a$10$3vjtlDjO6onnE.lROaAogegyX4fp3pPmgeJXusG6eknZT3Dz6nN42','08123456789','SUPERUSER','eef0cf24d249404f892f08ccf67bef48');
