UPDATE product
SET
    name = ?,
    description = ?,
    price = ?,
    stock = ?,
    image_url = ?,
    category = ?
WHERE id = ?;
