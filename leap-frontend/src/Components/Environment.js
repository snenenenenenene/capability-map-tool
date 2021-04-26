import React, {Component} from 'react';
import { Link } from 'react-router-dom';
import  { Redirect } from 'react-router-dom'
import axios from "axios"


export default class NewEnvironment extends Component
{
    constructor(props) {
        super(props);
        this.state = {
            };
    }

    componentDidMount() {
    }

    render() {
        return(
            <div>
                <h1>Home &gt; Environment</h1>
            </div>
        )
    }








}