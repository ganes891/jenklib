from flask import Flask, request, jsonify, render_template, redirect
from flask_sqlalchemy import SQLAlchemy
from flask_basicauth import BasicAuth
import os

app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///inventory.db'
app.config['BASIC_AUTH_USERNAME'] = 'admin'
app.config['BASIC_AUTH_PASSWORD'] = 'test@123'

db = SQLAlchemy(app)
basic_auth = BasicAuth(app)  # Initialize BasicAuth with the Flask app

class Product(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String(100), nullable=False)
    description = db.Column(db.String(200), nullable=True)
    price = db.Column(db.Float, nullable=False)
    category = db.Column(db.String(100), nullable=True)
    stock = db.Column(db.Integer, default=0)

def create_database():
    with app.app_context():
        db.create_all()

# Apply basic authentication to all routes
@app.route('/')
@basic_auth.required
def index():
    products = Product.query.all()
    return render_template('index.html', products=products)

@app.route('/product', methods=['POST'])
@basic_auth.required
def add_product():
    data = request.form
    new_product = Product(
        name=data['name'],
        description=data.get('description', ''),
        price=float(data['price']),
        category=data.get('category', ''),
        stock=int(data['stock'])
    )
    db.session.add(new_product)
    db.session.commit()
    return redirect('/')

@app.route('/delete/<int:id>', methods=['POST'])
@basic_auth.required
def delete_product(id):
    product = Product.query.get(id)
    if product:
        db.session.delete(product)
        db.session.commit()
    return redirect('/')

@app.route('/edit/<int:id>', methods=['GET'])
@basic_auth.required
def edit_product(id):
    product = Product.query.get(id)
    if product:
        return render_template('index.html', products=Product.query.all(), product_to_edit=product)
    return redirect('/')

@app.route('/update/<int:id>', methods=['POST'])
@basic_auth.required
def update_product(id):
    product = Product.query.get(id)
    if product:
        data = request.form
        product.name = data['name']
        product.description = data.get('description', '')
        product.price = float(data['price'])
        product.category = data.get('category', '')
        product.stock = int(data['stock'])
        db.session.commit()
    return redirect('/')

if __name__ == '__main__':
    create_database()
    app.run(host='0.0.0.0', port=int(os.environ.get('PORT', 5000)), debug=True)
