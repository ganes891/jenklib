from flask import Flask, request, jsonify
from flask_sqlalchemy import SQLAlchemy
import os

app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///inventory.db'
db = SQLAlchemy(app)

class Product(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String(100), nullable=False)
    stock = db.Column(db.Integer, default=0)

def create_database():
    with app.app_context():
        db.create_all()

@app.route('/product', methods=['POST'])
def add_product():
    data = request.json
    new_product = Product(name=data['name'], stock=data.get('stock', 0))
    db.session.add(new_product)
    db.session.commit()
    return jsonify({"message": "Product added successfully!"}), 201

@app.route('/products', methods=['GET'])
def get_products():
    products = Product.query.all()
    return jsonify([{ "id": p.id, "name": p.name, "stock": p.stock } for p in products])

@app.route('/product/<int:id>', methods=['PUT'])
def update_stock(id):
    data = request.json
    product = Product.query.get(id)
    if not product:
        return jsonify({"message": "Product not found!"}), 404
    product.stock = data['stock']
    db.session.commit()
    return jsonify({"message": "Stock updated successfully!"})

@app.route('/product/<int:id>', methods=['DELETE'])
def delete_product(id):
    product = Product.query.get(id)
    if not product:
        return jsonify({"message": "Product not found!"}), 404
    db.session.delete(product)
    db.session.commit()
    return jsonify({"message": "Product deleted successfully!"})

if __name__ == '__main__':
    create_database()
    app.run(host='0.0.0.0', port=int(os.environ.get('PORT', 5000)), debug=True)