import React, {Component} from 'react';
import { Link } from 'react-router-dom';
import  { Redirect } from 'react-router-dom'
import RecentEnvironment from './RecentEnvironments'
import axios from "axios"
import { Container, Col, Form, FormGroup, Label, Input } from 'reactstrap'
import RecentEnvironmentTableRow from "./RecentEnvironmentTableRow";
import styles from "../App.css"


export default class NewEnvironment extends Component
{
    constructor(props) {
        super(props);
        this.state = {
            environments: [],
            environmentName: '',
            };
        this.handleInputChange = this.handleInputChange.bind(this)
        this.handleSubmit = this.handleSubmit.bind(this);
    }

     handleSubmit = async e => {
        e.preventDefault();
        console.log("submit")
        const post_response = await fetch(`http://localhost:8080/environment/add`,
            {
            method: 'POST',
            headers:
                {
                    'Content-Type': "application/json",
                    'Accept': "application/json"},
            body: JSON.stringify({environmentName: this.state.environmentName}) });
        if (!post_response.ok) {
            console.log('Failed to upload via presigned POST');
        }
        console.log(`File uploaded via presigned POST with key: ${this.state.environmentName}`);
        this.props.history.push(`environment/${this.state.environmentName}`);
    }


    handleInputChange(event) {
        this.setState({ [event.target.name]: event.target.value })
    }

    componentDidMount() {
    }

    recentEnvironmentTableRow() {
        return this.state.environments.map((row, i) => {
            return <RecentEnvironmentTableRow obj={ row } key={ i }/>
        })
    }

    render() {
          return(
            <div class="jumbotron">
            <h1>Add Environment</h1>
            <div class="row">
                <div class="col-sm-6">
                    <div>
                        <p>Recent Environments</p>
                        <table className=' table table-striped'>
                            <thead>
                            <tr>
                                <th>Name</th>
                                <th>Description</th>
                            </tr>
                            </thead>
                            <tbody>
                            { this.recentEnvironmentTableRow() }
                            </tbody>
                        </table>
                    </div>
                </div>
                <div class="col-sm-6">
                <p>New Environments</p>
                <form className="form-inline" onSubmit={this.handleSubmit}>
                    <Input type="text" name="environmentName" value={this.state.environmentName} onChange={this.handleInputChange} className="form-control" placeholder="New Environment"/>
                    <button className="btn primary" type="button" onClick={this.handleSubmit}>Add</button>
                </form>
                </div>
            </div>
        </div>
        )
    }
}