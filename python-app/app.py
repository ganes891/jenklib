from flask import Flask, request, jsonify, render_template, redirect
from flask_sqlalchemy import SQLAlchemy
import os

app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///inventory.db'
db = SQLAlchemy(app)

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

@app.route('/')
def index():
    products = Product.query.all()
    return render_template('index.html', products=products)

@app.route('/product', methods=['POST'])
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
def delete_product(id):
    product = Product.query.get(id)
    if product:
        db.session.delete(product)
        db.session.commit()
    return redirect('/')

if __name__ == '__main__':
    create_database()
    app.run(host='0.0.0.0', port=int(os.environ.get('PORT', 5000)), debug=True)
