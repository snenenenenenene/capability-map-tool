import React, { Component } from 'react'
import { Link } from 'react-router-dom'
import axios from 'axios';
import toast from "react-hot-toast";

export default class RecentEnvironmentTableRow extends Component {

    constructor(props) {
        super(props);
        this.state = {
        };
    }

    deleteEnvironment (){
        axios.delete(`${process.env.REACT_APP_API_URL}/${this.props.obj.environmentId}`)
        .then(response => toast.success("Successfully Removed Environment"))
        .catch(error => {
            toast.error("Failed to Remove Environment")
            console.log(error)
        })
        window.location.reload();
    }

    render() {
        return (
            <tr>
                <td>
                    { this.props.obj.environmentId }
                </td>

                <td>
                    <Link to={ '/environment/' + this.props.obj.environmentName }>

                    { this.props.obj.environmentName }
        </Link>
                </td>
                <td>
                <button className='btn-sm btn-secondary'>
                    <i
                      onClick={() => {this.deleteEnvironment(this.props.obj.environmentId)}}
                      className='bi bi-trash'
                    ></i>
                  </button>
                </td>
            </tr>

        )
    }
}
