import React, {Component} from 'react';
import { Link } from 'react-router-dom';
import  { Redirect } from 'react-router-dom'
import axios from "axios"


export default class NewEnvironment extends Component
{
    constructor(props) {
        super(props);
        this.state = {
            redirect: null
            };
    }

    componentDidMount() {
    }

    newEnvironment (){
        console.log("soep")
        //const post_response = await fetch(`http://localhost:8080/addCapability`, { method: 'POST'});
        const environmentname = document.getElementById('environmentname').value;
        this.setState({ redirect: `/environment/${environmentname}`})
    }

    

    render() {
        if (this.state.redirect) {
            return <Redirect to={this.state.redirect} />
          }
          return(
            <div>
            <h1>Home &gt; New Environment</h1>
            <form onSubmit={this.newEnvironment} method="POST">
                <input type="text" id="environmentname" placeholder="New Environment"></input>
                
                <input type="button" value="Submit" onClick={() => this.newEnvironment()}></input>
                
            </form>
            
        </div>
        )
    }
}