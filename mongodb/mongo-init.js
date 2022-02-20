db.auth('root', 'example')

db = db.getSiblingDB('admin')

db.createUser({
    user: 'spark',
    pwd: 'spark',
    roles: [
      {
        role: 'dbOwner',
        db: 'admin',
      },
    ],
  });